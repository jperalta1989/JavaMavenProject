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
    $('.modal-container').show();
    $('.modal-container').css('z-index','10000');
    $('.sk-folding-cube').show();

}
function loaderHide() {
    $('.modal-container').hide();
    $('.modal-container').css('z-index','9999');
    $('.sk-folding-cube').hide();
}

function refreshDecodedTable(){
    tableJsonDecoded = JSON.parse(tableJson);
}

function populatePlayerData(){
    refreshDecodedTable();

    var player = tableJsonDecoded.players[newPlayerKey];


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
            populatePlayerData();
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
    $(this).parent('.player-container').attr('data-index',newPlayerKey);
    if(newPlayerKey === 1){
        $('#start-game').removeClass('disabled');
    }
});

$('.modal-close').click(function(){
    modalClose()
});


$('#start-game').click(function(){
    loaderShow();

    var url = '/ajax/game/start';

    var data = {
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
            runPreFlopDataPopulation();
            modalClose();
        },
        error: function(e){
            console.log(e);
        }
    });
});
function runPreFlopDataPopulation() {
    refreshDecodedTable();

    $(".player-container .active-player .cards").show();
    $('#start-game').hide();

    assignChipToPlayer(".dealer-chip", tableJsonDecoded.dealerIndex);
    assignChipToPlayer(".small-blind-chip", tableJsonDecoded.smallBlindIndex);
    assignChipToPlayer(".big-blind-chip", tableJsonDecoded.bigBlindIndex);

    for(var i = 1; i <= tableJsonDecoded.players.length; i++){
        $('#player' + i + ' .active-player .current-bet span').text(tableJsonDecoded.players[i-1].currentBet);
    };

    $('.current-bet').show();

    $('.pot span').text(tableJsonDecoded.pot);
}

function assignChipToPlayer(chip, index) {
    if(typeof index === "undefined"){
        return;
    }

    var className = "player" + (index + 1);
    console.log(className);
    var chipContainer = $(chip);
    chipContainer.show();
    chipContainer.addClass(className);
}