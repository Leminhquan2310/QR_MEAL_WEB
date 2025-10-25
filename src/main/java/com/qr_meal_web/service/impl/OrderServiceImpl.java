package com.qr_meal_web.service.impl;

import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.enums.TableStatus;
import com.qr_meal_web.model.*;
import com.qr_meal_web.repository.*;
import com.qr_meal_web.repository.impl.*;
import com.qr_meal_web.service.*;
import com.qr_meal_web.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository = new OrderRepositoryImpl();
    private final OrderStatusLogRepository orderStatusLogRepository = new OrderStatusLogRepositoryImpl();
    private final TableRepository tableRepository = new TableRepositoryImpl();

    @Override
    public boolean insertOrder(int table_id, CartService cart) {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // 1. Tạo order + order detail
            int order_id = orderRepository.insertOrder(connection, table_id, cart);


            for (CartItem cartItem : cart.getItems()) {
                orderDetailRepository.insertOrderDetail(connection, order_id, cartItem);
            }
            // 2. Cập nhật trạng thái bàn
            tableRepository.updateTableStatus(connection, table_id, TableStatus.OCCUPIED.getCode());

            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Order> selectAllOrder(int limit, int page) {
        int offset = (page - 1) * limit;
        return orderRepository.selectAllOrder(limit, offset);
    }

    @Override
    public Order selectOrderById(int id) {
        return orderRepository.selectOrderById(id);
    }

    @Override
    public Order selectOrderByTableIdAvailable(int id) {
        return orderRepository.selectOrderByTableIdAvailable(id);
    }

    @Override
    public boolean deleteOrder(int id) {
        boolean isReferenced = orderRepository.isReferenced(id);
        if (isReferenced) return false;
        return orderRepository.deleteOrder(id);
    }

    @Override
    public List<Order> filterOrder(int idOrTableId, int status, String createdFrom, String createdTo, int limit, int page) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (idOrTableId > 0) {
            sql.append(" AND (id = ? OR table_id = ?)");
            params.add(idOrTableId);
            params.add(idOrTableId);
        }

        if (status >= 0) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }

        int offset = (page - 1) * limit;
        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        return orderRepository.filterOrder(sql.toString(), params);
    }

    @Override
    public int getTotalOrders() {
        return orderRepository.countAll();
    }

    @Override
    public int getTotalOrdersFilter(int idOrTableId, int status, String createdFrom, String createdTo) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (idOrTableId > 0) {
            sql.append(" AND (id = ? OR table_id = ?)");
            params.add(idOrTableId);
            params.add(idOrTableId);
        }

        if (status >= 0) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }
        return orderRepository.countFilter(sql.toString(), params);
    }

    @Override
    public boolean changeOrderStatus(int orderId, int newStatus, Employee changedBy, String note) {
        Order order = orderRepository.selectOrderById(orderId);
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            orderRepository.changOrderStatus(connection, orderId, OrderStatus.fromCode(newStatus));

            // 3. Lưu log thay đổi
            OrderStatusLog log = new OrderStatusLog();
            log.setOrderId(orderId);
            log.setOld_status(order.getStatus());
            log.setNew_status(OrderStatus.fromCode(newStatus));
            log.setChanged_by(changedBy);
            log.setNote(note);
            orderStatusLogRepository.save(connection, log);

            TableStatus newTableStatus = TableStatus.fromOrderStatus(OrderStatus.fromCode(newStatus));
            tableRepository.updateTableStatus(connection, order.getTable_id(), newTableStatus.getCode());

            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean completeOrder(int orderId, Employee employee, double discount, String paymentMethod) {
        InvoiceService invoiceService = new InvoiceServiceImpl();
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            // 1. Lấy thông tin order hiện tại
            Order order = orderRepository.selectOrderById(orderId);
            if (order == null) return false;

            // 2. Cập nhật trạng thái order -> COMPLETED
            orderRepository.changOrderStatus(connection, orderId, OrderStatus.DONE);

            // 3. Lưu log thay đổi
            OrderStatusLog log = new OrderStatusLog();
            log.setOrderId(orderId);
            log.setOld_status(order.getStatus());
            log.setNew_status(OrderStatus.DONE);
            log.setChanged_by(employee);
            log.setNote("Hoàn tất hóa đơn");
            orderStatusLogRepository.save(connection, log);

            // 4. Cập nhật trạng thái bàn
            TableStatus newTableStatus = TableStatus.fromOrderStatus(OrderStatus.DONE);
            tableRepository.updateTableStatus(connection, order.getTable_id(), newTableStatus.getCode());

            // 5. Tạo hóa đơn
            invoiceService.createInvoice(connection, orderId, employee.getId(), discount, paymentMethod);

            // ✅ Commit toàn bộ transaction
            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
