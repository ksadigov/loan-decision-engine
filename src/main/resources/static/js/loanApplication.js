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
    .then(response => response.json())
    .then(data => {
        document.getElementById('applicationResult').classList.remove('hidden');
        document.getElementById('resultMaxAmount').textContent = data.maxAmountForRequestedPeriod;
        document.getElementById('resultApprovedAmount').textContent = data.approvedAmount;
        document.getElementById('resultApprovedPeriod').textContent = data.approvedPeriod;
        document.getElementById('resultMessage').textContent = data.message;
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});

document.getElementById('loanAmount').addEventListener('input', function() {
    const loanAmount = parseInt(this.value, 10);
    const feedbackElement = document.getElementById('loanAmountFeedback');

    if (loanAmount < 2000 || loanAmount > 10000) {
        feedbackElement.textContent = "Loan amount must be between 2,000 and 10,000.";
        feedbackElement.style.color = "red";
    } else {
        feedbackElement.textContent = "";
    }
});

document.getElementById('loanPeriod').addEventListener('input', function() {
    const loanPeriod = parseInt(this.value, 10);
    const feedbackElement = document.getElementById('loanPeriodFeedback');

    if (loanPeriod < 12 || loanPeriod > 60) {
        feedbackElement.textContent = "Loan period must be between 12 and 60 months.";
        feedbackElement.style.color = "red";
    } else {
        feedbackElement.textContent = "";
    }
});

