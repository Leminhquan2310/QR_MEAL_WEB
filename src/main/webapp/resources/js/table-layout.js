const gridSize = 10;
let undoStack = [];


fetch('/api/table')
    .then(res => res.json())
    .then(tables => {
        const layout = document.getElementById('layout-area');
        tables.forEach(t => {
            const div = document.createElement('div');
            div.classList.add('table-item', `status-${t.status.badge}`);
            div.textContent = t.name;
            div.dataset.id = t.id;
            div.style.left = `${t.pos_x * gridSize}px`;
            div.style.top = `${t.pos_y * gridSize}px`;
            div.style.width = `${t.width * gridSize}px`;
            div.style.height = `${t.height * gridSize}px`;
            layout.appendChild(div);
        });

        // === Kéo (drag) theo grid ===
        interact('.table-item').draggable({
            listeners: {
                start() {
                    saveState(); // ghi lại trạng thái trước khi thay đổi
                },
                move(event) {
                    const target = event.target;
                    const x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
                    const y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
                    target.style.transform = `translate(${x}px, ${y}px)`;
                    target.setAttribute('data-x', x);
                    target.setAttribute('data-y', y);
                },
                end(event) {
                    const target = event.target;
                    let left = parseInt(target.style.left) || 0;
                    let top = parseInt(target.style.top) || 0;
                    let offsetX = parseFloat(target.getAttribute('data-x')) || 0;
                    let offsetY = parseFloat(target.getAttribute('data-y')) || 0;

                    // Cộng khoảng di chuyển
                    left += offsetX;
                    top += offsetY;

                    // Làm tròn về lưới gần nhất
                    const snappedX = Math.round(left / gridSize) * gridSize;
                    const snappedY = Math.round(top / gridSize) * gridSize;

                    // Cập nhật lại vị trí thật
                    target.style.left = `${snappedX}px`;
                    target.style.top = `${snappedY}px`;
                    target.style.transform = 'translate(0, 0)';
                    target.setAttribute('data-x', 0);
                    target.setAttribute('data-y', 0);
                }
            },
            inertia: true,
            modifiers: [
                interact.modifiers.restrictRect({
                    restriction: 'parent',
                    endOnly: true
                })
            ]
        })

            // === Resize theo grid ===
            .resizable({
                edges: { left: true, right: true, bottom: true, top: true },
                listeners: {
                    start() {
                        saveState(); // ghi lại trạng thái trước khi thay đổi
                    },
                    move(event) {
                        const target = event.target;
                        let x = (parseFloat(target.getAttribute('data-x')) || 0);
                        let y = (parseFloat(target.getAttribute('data-y')) || 0);

                        target.style.width = `${event.rect.width}px`;
                        target.style.height = `${event.rect.height}px`;

                        x += event.deltaRect.left;
                        y += event.deltaRect.top;

                        target.style.transform = `translate(${x}px, ${y}px)`;
                        target.setAttribute('data-x', x);
                        target.setAttribute('data-y', y);
                    },
                    end(event) {
                        const target = event.target;
                        const width = parseFloat(target.style.width);
                        const height = parseFloat(target.style.height);

                        // Làm tròn kích thước theo grid
                        const snappedWidth = Math.round(width / gridSize) * gridSize;
                        const snappedHeight = Math.round(height / gridSize) * gridSize;

                        target.style.width = `${snappedWidth}px`;
                        target.style.height = `${snappedHeight}px`;
                    }
                },
                modifiers: [
                    // Giới hạn trong vùng layout
                    interact.modifiers.restrictSize({
                        min: { width: gridSize, height: gridSize },
                    }),
                    // Bám grid khi resize
                    interact.modifiers.snapSize({
                        targets: [
                            interact.snappers.grid({ x: gridSize, y: gridSize })
                        ],
                        range: gridSize / 2,
                        relativePoints: [{ x: 0, y: 0 }]
                    })
                ]
            });
    });


// === Nút lưu layout ===
document.getElementById('saveLayout').onclick = () => {
    const tablesUpdate = [...document.querySelectorAll('.table-item')].map(el => {
        const left = parseInt(el.style.left) + (parseFloat(el.getAttribute('data-x')) || 0);
        const top = parseInt(el.style.top) + (parseFloat(el.getAttribute('data-y')) || 0);

        return {
            id: el.dataset.id,
            pos_x: Math.round(left / gridSize),
            pos_y: Math.round(top / gridSize),
            width: Math.round(parseInt(el.style.width) / gridSize),
            height: Math.round(parseInt(el.style.height) / gridSize)
        };
    });

    console.log(tablesUpdate);

    fetch('/table?action=update-positions', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(tablesUpdate)
    }).then(() => alert('Lưu thành công!'));
};


document.getElementById('normalizeSize').onclick = async () => {
    const { value: formValues } = await Swal.fire({
        title: 'Đặt kích thước bàn',
        html: `
      <div style="display:flex; gap:10px; justify-content:center; align-items:center;">
        <input id="swal-width" type="number" class="swal2-input" placeholder="Rộng (px)" style="width:120px">
        <span style="font-size:20px;">×</span>
        <input id="swal-height" type="number" class="swal2-input" placeholder="Cao (px)" style="width:120px">
      </div>
    `,
        focusConfirm: false,
        confirmButtonText: 'Áp dụng',
        cancelButtonText: 'Hủy',
        showCancelButton: true,
        preConfirm: () => {
            const w = parseInt(document.getElementById('swal-width').value);
            const h = parseInt(document.getElementById('swal-height').value);
            if (!w || !h || w <= 0 || h <= 0) {
                Swal.showValidationMessage('❗ Vui lòng nhập kích thước hợp lệ');
                return false;
            }
            return { w, h };
        }
    });

    if (formValues) {
        document.querySelectorAll('.table-item').forEach(el => {
            el.style.width = `${formValues.w}px`;
            el.style.height = `${formValues.h}px`;
            el.style.lineHeight = `${formValues.h}px`;
        });

        Swal.fire({
            icon: 'success',
            title: '✅ Hoàn tất!',
            text: `Tất cả bàn đã được đặt về ${formValues.w}×${formValues.h}px.`,
            timer: 2000,
            showConfirmButton: false
        });
    }
};

function saveState() {
    const tablesState = [...document.querySelectorAll('.table-item')].map(el => ({
        id: el.dataset.id,
        left: el.style.left,
        top: el.style.top,
        width: el.style.width,
        height: el.style.height
    }));
    undoStack.push(JSON.stringify(tablesState));
}

function undo() {
    if (undoStack.length === 0) return;

    const lastState = undoStack.pop();
    restoreState(JSON.parse(lastState));
}


function restoreState(state) {
    state.forEach(saved => {
        const el = document.querySelector(`.table-item[data-id="${saved.id}"]`);
        if (!el) return;
        el.style.left = saved.left;
        el.style.top = saved.top;
        el.style.width = saved.width;
        el.style.height = saved.height;
        el.style.transform = 'translate(0,0)';
        el.setAttribute('data-x', 0);
        el.setAttribute('data-y', 0);
    });
}

document.getElementById('undoBtn').addEventListener('click', undo);