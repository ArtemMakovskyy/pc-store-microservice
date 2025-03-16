async function fetchKeyboards() {
    const response = await fetch('http://localhost:8092/api/keyboards');
    if (response.ok) {
        const data = await response.json();
        document.getElementById('keyboardsList').textContent = JSON.stringify(data, null, 2);
    } else {
        alert('Failed to fetch keyboards');
    }
}
