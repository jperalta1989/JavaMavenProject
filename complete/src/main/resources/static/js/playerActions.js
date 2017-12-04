var newPlayerId;
var newPlayerKey = -1;

function modalOpen(){
    $('.modal-container').show();
    $('.new-player-modal').show();

}

function modalClose(){
    loaderHide();
    $('.modal-container').hide();
    $('.new-player-modal').hide();

}

function loaderShow() {
    $('.modal-container').css('z-index','10000');
    $('.sk-folding-cube').show();

}
function loaderHide() {
    $('.modal-container').css('z-index','9999');
    $('.sk-folding-cube').hide();
}


function populatePlayerData(json){
    var table = JSON.parse(json);
    var player = table.players[newPlayerKey];

    console.log(player);

    $(newPlayerId + ' .active-player .username').text(player.username);
    $(newPlayerId + ' .active-player .balance').text('$' + player.balance.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));

    $(newPlayerId + ' .player-join').hide();
    $(newPlayerId + ' .active-player').show();

}

$('#player-join-submit').click(function(){
    loaderShow();

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
    $(this).parent('.player-container').removeClass('available');
    newPlayerKey++;
    if(newPlayerKey === 1){
        $('#start-game').removeClass('disabled');
    }
});

$('.modal-close').click(function(){
    modalClose()
});


$('#start-game').click(function(){

});
