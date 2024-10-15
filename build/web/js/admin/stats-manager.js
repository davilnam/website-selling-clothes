/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// Single Bar Chart
// Declare a variable to store the chart instance
let myChart;

document.getElementById('filterButton').addEventListener('click', function () {
    const fromDate = document.getElementById('fromDate').value;
    const toDate = document.getElementById('toDate').value;
    const filterCriteria = document.getElementById('filterCriteria').value;

    fetch('stats', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({fromDate, toDate, filterCriteria})
    })
            .then(response => response.json())
            .then(data => {
                console.log(data);

                const ctx = document.getElementById('bar-chart').getContext('2d');

                // Check if myChart exists and destroy it
                if (myChart) {
                    myChart.destroy();
                }

                // Create a new chart instance and store it in myChart
                myChart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: data.labels,
                        datasets: [{
                                label: 'Total Amount',
                                data: data.amounts,
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                borderColor: 'rgba(75, 192, 192, 1)',
                                borderWidth: 1
                            }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            });
});
