async function updateSellingPrice() {
    const id = document.getElementById('keyboardId').value;
    const newSellingPrice = document.getElementById('newSellingPrice').value;

    if (!id || !newSellingPrice) {
        alert('Please enter both Keyboard ID and New Selling Price');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8092/api/keyboards/${id}/selling-price?newSellingPrice=${newSellingPrice}`, {
            method: 'PATCH',
        });

        if (response.ok) {
            const updatedKeyboard = await response.json();
            alert(`Selling price updated successfully! New details:\n${JSON.stringify(updatedKeyboard, null, 2)}`);
        } else if (response.status === 404) {
            alert('Keyboard not found!');
        } else {
            alert('Failed to update selling price.');
        }
    } catch (error) {
        console.error('Error updating selling price:', error);
        alert('An unexpected error occurred.');
    }
}
