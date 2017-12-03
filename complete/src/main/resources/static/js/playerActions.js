var newPlayerId;
var newPlayerKey = -1;

function modalOpen(){
    $('.modal-container').show();
}

function modalClose(){
    $('.modal-container').hide();
}

function populatePlayerData(json){
    var table = JSON.parse(json);
    var player = table.players[newPlayerKey];

    console.log(player);

    $(newPlayerId + ' .active-player .username').text(player.username);
    $(newPlayerId + ' .active-player .balance').text(player.balance);

    $(newPlayerId + ' .player-join').hide();
    $(newPlayerId + ' .active-player').show();

}

$('#player-join-submit').click(function(){
    var url = $('#player-join-form').attr('action');

    var data = {
        username : $('#player-join-form #username').val(),
        balance : $('#player-join-form #balance').val(),
        tableJson : tableJson
    };

    $.ajax({
        url : url,
        method : 'GET',
        data : data,
        cache:false,
        contentType: 'application/json; charset=utf-8',
        success: function(msg){
            tableJson = msg;
            populatePlayerData(msg);
            modalClose();
        },
        error: function(e){
            console.log(e);
        }
    });
});

$('.player-join').click(function(){

   modalOpen();

    newPlayerId = '#' + $(this).parent('.player-container').attr("id");
    newPlayerKey++;
});

$('.modal-close').click(function(){
    modalClose()
});