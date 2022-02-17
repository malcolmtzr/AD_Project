$(document).ready(function() {
    $("#tblHeader").hide();
    $("#sent").click(function (e) {
        $("#sent").removeClass("btn btn-secondary");
        $("#sent").addClass("btn btn-primary");
        $("#received").removeClass();
        $("#received").addClass("btn btn-secondary");
        e.preventDefault();
        $("#requestsTbl").empty();
        document.getElementById("req_display").innerHTML = "Sent Requests";
        $.ajax({
            method: 'GET',
            url: "http://localhost:8080/api/friends/requests",
            data: {
                username: $("#username").val(),
                sent: "true"
            },
            success: function(response) {
                console.log(response);
                buildTable_sent(response)
            }
        });
    });
    $("#received").click (function (e, msg) {
        e.preventDefault();
        $("#received").removeClass("btn btn-secondary");
        $("#received").addClass("btn btn-primary");
        $("#sent").removeClass();
        $("#sent").addClass("btn btn-secondary");
        $("#requestsTbl").empty();
        document.getElementById("req_display").innerHTML = "Received Requests";
        $.ajax({
            method: 'GET',
            url: "http://localhost:8080/api/friends/requests",
            data: {
                username: $("#username").val(),
                sent: "false"
            },
            success: function(response) {
                console.log(response);
                buildTable_received(response)
            }
        });
    });
});

function buildTable_sent(data) {
    $("#tblHeader").show();
    var tableData = "";
    data.forEach(function (user) {
        tableData += '<tr>' +
            '<td><img id="thumbnail" style="width: 100px" src="/images/' + user.userId + '/' + user.profilePic + '"/>' + '</td>' +
            '<td>' + user.name + '</td>' +
            '<td>' + user.username + '</td>' +
            '<td id="action_space">' +
            '<button id="delete_req' + user.userId + '" class="btn btn-danger" value="' + user.username + '">Delete Request</button>' +
            '</td>' +
            '</tr>';
    });

    $("#tblId>tbody").html(tableData);

    data.forEach(function(user) {
        $('#delete_req'+user.userId).click(function (e) {
            e.preventDefault();
            $.ajax({
                method: 'GET',
                url: "http://localhost:8080/api/friends/request/process",
                data: {
                    username: $("#username").val(),
                    sender: $("#delete_req"+user.userId).val(),
                    action: "delete"
                },
                success: function (response) {
                    console.log(response);
                    // var obj = JSON.parse(response);
                    // var msg = obj['message'];
                    document.getElementById("sent").click();
                }
            });
        })
    })
}

function buildTable_received(data) {
    $("#tblHeader").show();
    var tableData = "";
    data.forEach(function (user) {
        tableData += '<tr>' +
            '<td><img id="thumbnail" style="width: 100px" src="/images/' + user.userId + '/' + user.profilePic + '"/></td>' +
            '<td>' + user.name + '</td>' +
            '<td id="sender_username">' + user.username + '</td>'+
            '<td>' +
                '<div id="action_space">' +
                    '<button id="accept_req' + user.userId +'" class="btn btn-success" value="' + user.username +'">Accept</button>' + '\n' +
                    '<button id="reject_req' + user.userId +'" class="btn btn-danger" value="' + user.username +'">Reject</button>' +
                '</div>' +
            '</td>' +
        '</tr>';
    });

    $("#tblId>tbody").html(tableData);

    data.forEach(function(user) {
        $('#accept_req'+user.userId).click(function (e) {
            e.preventDefault();
            $.ajax({
                method: 'GET',
                url: "http://localhost:8080/api/friends/request/process",
                data: {
                    username: $("#username").val(),
                    sender: $("#accept_req" + user.userId).val(),
                    action: "accept"
                },
                success: function (response) {
                    console.log(response);
                    // var obj = JSON.parse(response);
                    // var msg = obj['message'];
                    document.getElementById("sent").click();
                }
            });
        });
        $('#reject_req'+user.userId).click(function(e) {
            e.preventDefault();
            $.ajax({
                method: 'GET',
                url: "http://localhost:8080/api/friends/request/process",
                data: {
                    username: $("#username").val(),
                    sender: $("#reject_req" + user.userId).val(),
                    action: "reject"
                },
                success: function (response) {
                    console.log(response);
                    // var obj = JSON.parse(response);
                    // var msg = obj['message'];
                    document.getElementById("received").click();
                }
            });
        });
    })
}


