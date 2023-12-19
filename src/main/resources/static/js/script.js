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
        $("search-result").hide();
    }else {
        console.log(query);

        // sending request to server
        let url=`http://localhost:8080/search/${query}`;
        fetch(url).then((response)=>{
            return response.json();
        }).then(data=>{
            // data......
        });
        $("search-result").show();
    }
}