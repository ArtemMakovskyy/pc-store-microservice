async function deleteKeyboard() {
    const id = document.getElementById('keyboardId').value;
    if (!id) {
        alert('Please enter a Keyboard ID');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8092/api/keyboards/${id}`, {
            method: 'DELETE'
        });

        if (response.status === 204) {
            alert('Keyboard deleted successfully!');
            document.getElementById('keyboardId').value = '';
            window.location.href = "keyboard-management.html";
        } else if (response.status === 404) {
            alert('Keyboard not found!');
        } else {
            alert('An error occurred while deleting the keyboard.');
        }
    } catch (error) {
        console.error('Error deleting keyboard:', error);
        alert('An unexpected error occurred.');
    }
}
