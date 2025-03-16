async function getKeyboard() {
    const id = document.getElementById('keyboardId').value;
    const response = await fetch(`http://localhost:8092/api/keyboards/${id}`);

    if (response.ok) {
        const data = await response.json();
        document.getElementById('keyboardInfo').textContent = JSON.stringify(data, null, 5);
    } else {
        alert('Keyboard not found');
    }
}
