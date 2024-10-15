/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function changeColor(input) {
    let selectedColor = input.value;
    var productId = input.getAttribute('data-product-id');
    fetch(`product-detail?id=${productId}&action=changeColor&selectedColor=${selectedColor}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }        
    })
    .then(function(response) {
        console.log(response);
        if (response.ok) {              
            window.location.reload();
            console.log("thanh cong");
        } 
    })
    .catch(function(error) {
        console.error('Lỗi:', error);
    });
}

//function changeSize(input) {
//    let selectedSize = input.value;
//    var productId = input.getAttribute('data-product-id');
//    fetch(`product-detail?id=${productId}&action=changeSize&selectedSize=${selectedSize}`, {
//        method: 'GET',
//        headers: {
//            'Content-Type': 'application/x-www-form-urlencoded'
//        }        
//    })
//    .then(function(response) {
//        console.log(response);
//        if (response.ok) {              
//            window.location.reload();
//            console.log("thanh cong");
//        } 
//    })
//    .catch(function(error) {
//        console.error('Lỗi:', error);
//    });
//}