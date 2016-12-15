<html>
<head>
    <title>Ping</title>
    <meta charset="UTF-8"/>
</head>

<body>
<h1>ping</h1>
<form action="#">
    目的主机:
    <br>
    <input id="dest-ip" type="text" maxlength="20" name="destIp"></input>
    <input id="submit-button" type="button" value="提交">
    <input id="stop-button" type="button" value="停止">
</form>
<p id="response">

</p>
</body>

<script src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>

<script>
    var running = true;
    $("#submit-button").click(function () {
        var ip = $("#dest-ip").val()
        running = true;
        setInterval(function(){
            if(running){
                $.get({
                          url: "/ping?destIp=" + ip,
                          success: function (data) {
                              if(data != ''){
                                  $("#response").append(data + '<br>')
                              }
                          }
                      });
            }
        },500);
    });


    $("#stop-button").click(function () {
        var ip = $("#dest-ip").val()
        $.get({
            url:"/stop?destIp="+ip,
            success:function (data) {
                running = false;
                alert(data);
            }
              });
    });
</script>
</html>