<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="col-auto d-flex gap-2 mb-2">
    <a href="/table?page=1" class="btn btn-secondary"> ↩ Quay lại</a>
    <button id="saveLayout" class="btn btn-secondary">💾 Lưu bố cục</button>
    <button id="normalizeSize" class="btn btn-secondary">📏 Đặt kích thước đều</button>
    <button id="undoBtn" class="btn btn-secondary">↩️ Undo</button>
</div>
<div id="layout-area">
</div>

<script src="https://cdn.jsdelivr.net/npm/interactjs/dist/interact.min.js"></script>