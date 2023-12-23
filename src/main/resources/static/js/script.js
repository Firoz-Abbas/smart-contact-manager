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
        alert("amount is required !!");
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
        },
        error:function(error){
            // invoded when error
            console.log(error)
            alert("Somthing went wrong !!")
        }
    }
    )

};