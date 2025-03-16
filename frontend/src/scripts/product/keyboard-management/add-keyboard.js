async function submitKeyboard() {
    const data = {
        partNumber: document.getElementById('partNumber').value,
        name: document.getElementById('name').value,
        description: document.getElementById('description').value,
        costPrice: parseFloat(document.getElementById('costPrice').value),
        sellingPrice: parseFloat(document.getElementById('sellingPrice').value)
    };

    try {
        const response = await fetch('http://localhost:8092/api/keyboards', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Keyboard added successfully!');
            window.location.href = "../../../index.html";
        } else {
            alert('Failed to add keyboard');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An unexpected error occurred while adding the keyboard.');
    }

}
