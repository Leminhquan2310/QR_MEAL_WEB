const notyf = new Notyf({
    duration: 0,         // không tự ẩn
    dismissible: true,   // có nút đóng
});

function showOrderToast(data) {
    const notification = notyf.success(data.message);
    notification.on('click', ({target, event}) => {
        window.location.href = `/dashboard?highlight=${data.table_id}`;
    });
}