/**
 * Hàm debounce giúp trì hoãn việc thực thi hàm callback
 * cho đến khi người dùng ngừng thao tác trong một khoảng thời gian nhất định.
 *
 * @param {Function} func - Hàm cần được trì hoãn
 * @param {number} delay - Thời gian chờ (ms)
 * @returns {Function} - Hàm được "bọc" debounce
 */
function debounce(func, delay = 500) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => func.apply(this, args), delay);
    };
}