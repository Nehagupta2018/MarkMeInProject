function validateMail(element) {
    var mailSyntax = /^[A-Z0-9._%+-]+@([A-Z0-9-]+\.)+[A-Z]{2,4}$/i;
    if (element.value !== "") {
        if (!element.value.match(mailSyntax)) {
            alert("Please enter a valid email.");
            element.value = "";
        }
    }
}

function validatePassword(element) {
    var passwordSyntax = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
    if (element.value !== "") {
        if (!element.value.match(passwordSyntax)) {
            alert("Password should contain atleast a symbol, upper and lower case letters and a number. Please enter again.");
            element.value = "";
        }
    }
}

function validateContact(element) {
    var contactSyntax = /^([1-9]{1})\d{9}$/;
    if (element.value !== "") {
        if (!element.value.match(contactSyntax)) {
            alert("Please enter a valid phone number.");
            element.value = "";
        }
    }
}