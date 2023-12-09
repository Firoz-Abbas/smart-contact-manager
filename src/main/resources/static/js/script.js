console.log("this is script file")

const toggleSidebar = () => {
    if($(".sidebar").is(":visible")){
    // do close 
        $(".sidebarr").css("display","block");
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","5%");
    }else{
    // do show
        $(".sidebar").css("display","block");
        $(".sidebarr").css("display","none");
        $(".content").css("margin-left","20%");
    }
}