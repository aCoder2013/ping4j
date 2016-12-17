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
    自定义参数:
    <input id="custom-param" type="text" maxlength="25"></input>
    <br>
    次数
    <input id="count" type="number" maxlength="5" value="50"></input>
    <input id="submit-button" type="button" value="开始">
    <input id="pause-button" type="button" value="暂停">
</form>
<p id="response">

</p>
</body>

<script src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>

<script>
    var running = true;
    $("#submit-button").click(function () {
        var ip = $("#dest-ip").val();
        var count = $("#count").val();
        var customParam = $("#custom-param").val();
        running = true;
        setInterval(function () {
            if (running) {
                $.get({
                          url: "/ping?destIp=" + ip + "&count=" + count + "&custom-param="
                               + customParam
                               + customParam,
                          success: function (data) {
                              if (data.success) {
                                  if (data.data != '') {
                                      $("#response").append(data.data + '<br>')
                                  }
                              }else {
                                  running = false;
                                  alert("任务结束");
                              }
                          }
                      });
            }
        }, 200);
    });

    $("#pause-button").click(function () {
        running = false;
    });

    $(document).keypress("c", function (e) {
        var ip = $("#dest-ip").val()
        if (e.ctrlKey) {
            $.get({
                      url: "/stop?destIp=" + ip,
                      success: function (data) {
                          running = false;
                          alert("已经取消!");
                      }
                  });
        }
    });

</script>
</html>