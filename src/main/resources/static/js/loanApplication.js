document.getElementById('applyNow').addEventListener('click', function() {
    const loanAmount = document.getElementById('loanAmount').value;
    const loanPeriod = document.getElementById('loanPeriod').value;

    if (loanAmount < 2000 || loanAmount > 10000) {
        alert("Loan amount must be between 2,000 and 10,000.");
        return;
    }

    if (loanPeriod < 12 || loanPeriod > 60) {
        alert("Loan period must be between 12 and 60 months.");
        return;
    }

    const personalCode = document.getElementById('personalCode').value;
    const data = { personalCode, loanAmount, loanPeriod };

    fetch('/api/v1/loans/decision', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                throw new Error(errorData.message || "An error occurred");
            });
        }
        return response.json();
    })
    .then(data => {
        document.getElementById('applicationResult').classList.remove('hidden');
        document.getElementById('resultAmount').textContent = data.amount;
        document.getElementById('resultPeriod').textContent = data.period;
        document.getElementById('resultStatus').textContent = data.status;
    })
    .catch((error) => {
        alert("Error: " + error.message);
        console.error('Error:', error);
    });
});
