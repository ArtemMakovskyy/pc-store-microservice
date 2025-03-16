async function loadKeyboard() {
    const id = document.getElementById('keyboardId').value;
    if (!id) {
        alert('Please enter a Keyboard ID');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8092/api/keyboards/${id}`);
        if (response.ok) {
            const keyboard = await response.json();

            document.getElementById('partNumber').value = keyboard.partNumber || '';
            document.getElementById('name').value = keyboard.name || '';
            document.getElementById('description').value = keyboard.description || '';
            document.getElementById('costPrice').value = keyboard.costPrice || 0;
            document.getElementById('sellingPrice').value = keyboard.sellingPrice || 0;

            document.getElementById('keyboardForm').style.display = 'block';
        } else {
            alert('Keyboard not found!');
        }
    } catch (error) {
        console.error('Error loading keyboard:', error);
        alert('An error occurred while loading the keyboard.');
    }
}

async function submitUpdatedKeyboard() {
    const id = document.getElementById('keyboardId').value;
    const data = {
        partNumber: document.getElementById('partNumber').value,
        name: document.getElementById('name').value,
        description: document.getElementById('description').value,
        costPrice: parseFloat(document.getElementById('costPrice').value),
        sellingPrice: parseFloat(document.getElementById('sellingPrice').value)
    };

    try {
        const response = await fetch(`http://localhost:8092/api/keyboards/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Keyboard updated successfully!');
        } else {
            alert('Failed to update the keyboard.');
        }
    } catch (error) {
        console.error('Error updating keyboard:', error);
        alert('An error occurred while updating the keyboard.');
    }
}