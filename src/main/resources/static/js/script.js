console.log("this is script file")

const toggleSidebar = () => {
    if($(".sidebar").is(":visible")){
    // do close 
    //     $(".sidebarr").css("display","block");
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","15px");
    }else{
    // do show
        $(".sidebar").css("display","block");
        // $(".sidebarr").css("display","none");
        $(".content").css("margin-left","208px");
    }
}


function myFunction() {
    var txt;
    if (confirm("Are you sure!")) {
        txt = "You pressed OK!";
    } else {
        txt = "You pressed Cancel!";
    }
    document.getElementById("demo").innerHTML = txt;
}

const search=()=>{
    // console.log("searching.....");
    let query=$("#search-input").val();
    if (query==''){
        $(".search-result").hide();
    }else {
        console.log(query);

        // sending request to server
        let url=`http://localhost:8080/search/${query}`;
        fetch(url).then((response)=>{
            return response.json();
        }).then(data=>{
            // data......
            console.log(data);
            let text =`<div class="list-group">`;
            data.forEach((contact) =>{
                text += `<a href="/user/${contact.cId}/contact" class="list-group-item list-group-item-action">${contact.name}</a>`;
            })
            text+=`</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });

    }
}

// firt request to server to create order of payment

const paymentStart=()=>{
    console.log("payment started");
    let amount=$("#payment_field").val();
    console.log("payment amount "+amount);

    if(amount=="" || amount==null){
        // alert("amount is required !!");
        swal(" Failed!", "amount is required !!", "error");

        return;
    }

    // code .........
    // we will use ajex to send request to server to create order

    $.ajax(
        {
        url:'/user/create_order',
        data:JSON.stringify({amount:amount, info:'order_request'}),
        contentType:'application/json',
        type:'POST',
        dataType:'json',
        success:function(response){
            // invoked when success
            console.log(response)
            if (response.status=='created'){
            //    open payment form
                let options = {
                    key: "rzp_test_EbhFzl2CXAYgz4", // Enter the Key ID generated from the Dashboard
                    amount: response.amount, // Amount is in currency subunits. Default currency is
                    currency: "INR",
                    name: "Smart contact manager",
                    description: "Test Transaction",
                    image: "https://resources.edunexttechnologies.com/html-team/common-images/cbse-logo-blue.png",
                    order_id: response.id, //This is a sample Order ID. Pass the

                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);

                        updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid");
                        // swal(" Good job!", "Congrates !! Payment successful !!", "success");

                    },
                    "prefill": {
                        name: "",
                        email: "",
                        contact: "",
                    },
                    notes: {
                        address: "Learn with me",
                    },
                    theme: {
                        color: "#3399cc",
                    },
                };

                var rzp = new Razorpay(options);
                rzp.on('payment.failed', function (response){
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    // alert("Oops Payment failed !!")
                    swal(" Failed!", "Oops Payment failed !!", "error");
                });

                rzp.open();

            }
        },
        error:function(error){
            // invoded when error
            console.log(error)
            // alert("Somthing went wrong !!")
            swal(" Failed!", "Somthing went wrong !!", "error");
        }
    }
    )

};


function updatePaymentOnServer(payment_id, order_id, status) {
    $.ajax({
        url:'/user/update_order',
        data:JSON.stringify({payment_id:payment_id, order_id:order_id, status:status}),
        contentType:'application/json',
        type:'POST',
        dataType:'json',

        success:function (response) {
            swal(" Good job!", "Congrates !! Payment successful !!", "success");
        },
        error:function (error) {
            swal(" Failed!", "Your payment is successful, but we did not get on server, we will contact you as soon as possible !!", "error");
        },
    });
    
}