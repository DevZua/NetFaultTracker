document.addEventListener('DOMContentLoaded', function () {
    const issueLinks = document.querySelectorAll('.issue-link');

    issueLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const issueId = this.getAttribute('data-id');

            fetch(`/issues/api/${issueId}`)
                .then(response => response.json())
                .then(data => {
                    document.querySelector('#issue-details').innerHTML = `
                        <h2>ID: ${data.id}</h2>
                        <p><strong>Title:</strong> ${data.title}</p>
                        <p><strong>Description:</strong> ${data.description}</p>
                        <p><strong>Status:</strong> ${data.status}</p>
                        <p><strong>Created At:</strong> ${data.createdAt}</p>
                    `;
                })
                .catch(error => console.error('Error:', error));
        });
    });
});
