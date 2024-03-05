document.getElementById('applyNow').addEventListener('click', function() {
    const personalCode = document.getElementById('personalCode').value;
    const loanAmount = document.getElementById('loanAmount').value;
    const loanPeriod = document.getElementById('loanPeriod').value;

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
        console.log(data); // Add this line to log the response for debugging
        document.getElementById('applicationResult').classList.remove('hidden');
//        document.getElementById('resultApproved').textContent = data.approved ? 'Yes' : 'No';
        document.getElementById('resultMaxAmount').textContent = data.maxAmountForRequestedPeriod;
        document.getElementById('resultApprovedAmount').textContent = data.approvedAmount;
        document.getElementById('resultApprovedPeriod').textContent = data.approvedPeriod;
        document.getElementById('resultMessage').textContent = data.message;
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});
