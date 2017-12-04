var newPlayerId;
var newPlayerKey = -1;
var currentTurnIndex = -1;
var nextStep;
var previousTableJsonVersion;
var playersHadATurn = -1;

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
    $('.sk-folding-cube').show();

}
function loaderHide() {
    $('.modal-container').hide();
    $('.sk-folding-cube').hide();
}

function nextPlayerTurnShow(){
    $('.modal-container').show();
    $('.next-player-turn').show();
}

function nextPlayerTurnHide(){
    $('.modal-container').hide();
    $('.next-player-turn').hide();
}

function refreshDecodedTable(){
    tableJsonDecoded = JSON.parse(tableJsonLocal);
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
        tableJson : tableJsonLocal
    };

    $.ajax({
        url : url,
        method : 'GET',
        data : data,
        async: false,
        success: function(msg){
            tableJsonLocal = msg;
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

    encodeDecodedTable();
    var data = {
        tableJson : tableJsonLocal
    };

    $.ajax({
        url : url,
        method : 'GET',
        data : data,
        async : false,
        success: function(msg){
            tableJsonLocal = msg;
            runPreFlopDataPopulation();
            loaderHide();
        },
        error: function(e){
            console.log(e);
        }
    });
});

$('#start-new-game').click(function(){
    loaderShow();

    var url = '/ajax/game/reset';

    var data = {
        tableJson : previousTableJsonVersion
    };

    encodeDecodedTable();
    $.ajax({
        url : url,
        method : 'GET',
        data : data,
        async :false,
        success: function(msg){
            tableJsonLocal = msg;
            runPreFlopDataPopulation();
            loaderHide();
        },
        error: function(e){
            console.log(e);
        }
    });
});


$('.next-player-turn .button').click(function(){
    var currentPlayer = getCurrentBetter();

    if(currentPlayer.isFolded){
        currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
        displayCurrentPlayerContinueModal();
    }

    if (currentPlayer.currentBet == tableJsonDecoded.amountToCall){
        $('#check').show();
        $('#call').hide();
    } else {
        $('#call').show();
        $('#check').hide();
    }

    var callDiference = tableJsonDecoded.amountToCall - currentPlayer.betAmount;
    $('#call span').text(callDiference);

    nextPlayerTurnHide();

    var betterId = 'player' + (currentTurnIndex + 1);
    var leftCardSrc = "/images/" + currentPlayer.holeCards[0].cardImg;
    var rightCardSrc = "/images/" + currentPlayer.holeCards[1].cardImg;

    $('.player-container').css('opacity', '.5');
    $("#" + betterId).css('opacity', '1');
    $('.player-action-container .button-container #call span').text(tableJsonDecoded.amountToCall);
    $('.player-action-container').addClass(betterId);
    $("#" + betterId + " .active-player .cards .left-card").attr('src', leftCardSrc);
    $("#" + betterId + " .active-player .cards .right-card").attr('src', rightCardSrc);
});

$('#fold').click(function(){
    tableJsonDecoded.players[currentTurnIndex].isFolded = true;
    tableJsonDecoded.activePlayers = tableJsonDecoded.activePlayers - 1;
    currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
    displayCurrentPlayerContinueModal();
});

$('#call').click(function(){
    var callDifference = tableJsonDecoded.amountToCall - tableJsonDecoded.players[currentTurnIndex].currentBet;

    if(callDifference > 0){
        var playerHtmlIndex = currentTurnIndex + 1;
        tableJsonDecoded.players[currentTurnIndex].balance = tableJsonDecoded.players[currentTurnIndex].balance - callDifference;
        tableJsonDecoded.players[currentTurnIndex].currentBet = tableJsonDecoded.amountToCall;
        $('#player' + playerHtmlIndex + ' .active-player .current-bet span').text(tableJsonDecoded.players[currentTurnIndex].currentBet);
        $('#player' + playerHtmlIndex + ' .active-player .balance').text(tableJsonDecoded.players[currentTurnIndex].balance);


        tableJsonDecoded.pot = tableJsonDecoded.pot + callDifference;
        updatePot();
    }

    currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
    displayCurrentPlayerContinueModal();
});

$('#check').click(function () {
    currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
    displayCurrentPlayerContinueModal();
});

$('#bet').click(function(){
    $('#bet-amount-container').show();
});

$('#bet-amount-container .button').click(function(){
    var betAmount = parseInt($('#bet-amount-container #betAmount').val());
    tableJsonDecoded.players[currentTurnIndex].balance = tableJsonDecoded.players[currentTurnIndex].balance - betAmount;
    tableJsonDecoded.players[currentTurnIndex].currentBet = tableJsonDecoded.players[currentTurnIndex].currentBet + betAmount;

    var playerHtmlIndex = currentTurnIndex + 1;

    $('#player' + playerHtmlIndex + ' .active-player .current-bet span').text(tableJsonDecoded.players[currentTurnIndex].currentBet);
    $('#player' + playerHtmlIndex + ' .active-player .balance').text(tableJsonDecoded.players[currentTurnIndex].balance);

    tableJsonDecoded.pot = tableJsonDecoded.pot + betAmount;
    updatePot();
    tableJsonDecoded.amountToCall = tableJsonDecoded.amountToCall + betAmount;
    currentTurnIndex = getNextValidatedPlayerIndex(currentTurnIndex);
    displayCurrentPlayerContinueModal();
});

function runPreFlopDataPopulation() {
    refreshDecodedTable();

    $(".player-container .active-player .cards").show();
    $('#start-game').hide();
    $('#start-new-game').hide();

    assignChipToPlayer(".dealer-chip", tableJsonDecoded.dealerIndex);
    assignChipToPlayer(".small-blind-chip", tableJsonDecoded.smallBlindIndex);
    assignChipToPlayer(".big-blind-chip", tableJsonDecoded.bigBlindIndex);

    for (var i = 1; i <= tableJsonDecoded.players.length; i++) {
        $('#player' + i + ' .active-player .balance').text(tableJsonDecoded.players[i -1].balance);
        $('#player' + i + ' .active-player .current-bet span').text(tableJsonDecoded.players[i - 1].currentBet);
    }

    $('.current-bet').show();

    updatePot();

    currentTurnIndex = tableJsonDecoded.firstToBetIndex;

    displayCurrentPlayerContinueModal();

    nextStep = 'flop';
}

function assignChipToPlayer(chip, index) {
    if(typeof index === "undefined"){
        return;
    }

    var className = "player" + (index + 1);
    var chipContainer = $(chip);
    chipContainer.show();
    chipContainer.addClass(className);
}

function updatePot(){
    $('.pot span').text(tableJsonDecoded.pot);
}

function displayCurrentPlayerContinueModal(){
    if(isBettingOver()){
        runNextStep();
    } else{
        resetActionContainer();
        $('#bet-amount-container').hide();
        $(".active-player .cards img").attr('src', cardBackSrc);
        var currentBetter = getCurrentBetter();
        $('.next-player-turn h1').text(currentBetter.username);
        nextPlayerTurnShow();
    }

}

function resetActionContainer(){
    var actionContainer =  $('.player-action-container');

    actionContainer.removeClass('player1');
    actionContainer.removeClass('player2');
    actionContainer.removeClass('player3');
    actionContainer.removeClass('player4');
}

function isBettingOver(){
    playersHadATurn++;

    if(playersHadATurn < tableJsonDecoded.activePlayers){
        return false;
    }

    for (var i = 0; i < tableJsonDecoded.players.length; i++) {
        if(tableJsonDecoded.players[i].currentBet != tableJsonDecoded.amountToCall){
            return false;
        }
    }

    playersHadATurn = -1;
    return true;
}

function getCurrentBetter(){
    return tableJsonDecoded.players[currentTurnIndex];
}

function getNextValidatedPlayerIndex(index){
    var newIndex = (index + 1) % tableJsonDecoded.players.length;
    return newIndex;
}

function runNextStep(){
    switch (nextStep){
        case "flop":
            runStep('/ajax/game/flop', 'turn');
            break;
        case "turn":
            runStep('/ajax/game/turn', 'river');
            break;
        case "river":
            runStep('/ajax/game/river', 'showdown');
            break;
        case "showdown":
            runStep('/ajax/game/showdown', 'complete');
            break;
        case "complete":
            break;
        default:
            break;
    }
}

function runStep(url, newStep){
    loaderShow();

    previousTableJsonVersion = tableJsonLocal;

    encodeDecodedTable();
    var data = {
        tableJson : tableJsonLocal
    };

    $.ajax({
        url : url,
        method : 'GET',
        data : data,
        async:false,
        success: function(msg){
            tableJsonLocal = msg;
            nextStep = newStep;
            placeCommunityCardsOnTable();
            loaderHide();
        },
        error: function(e){
            console.log(e);
        }
    });
}

function placeCommunityCardsOnTable() {
    if (nextStep == 'complete'){
        endGame();
    } else{

        var imgSrc;

        refreshDecodedTable();
        resetActionContainer();
        $('#bet-amount-container').hide();
        $(".active-player .cards img").attr('src', cardBackSrc);

        for(var i = 1; i <= tableJsonDecoded.communityCards.length; i++){
            imgSrc = '/images/' + tableJsonDecoded.communityCards[i - 1].cardImg;
            $('.community-cards #card' + i).attr('src', imgSrc);
        }
        currentTurnIndex = tableJsonDecoded.firstToBetIndex;
        nextPlayerTurnShow();
    }
}

function endGame(){
    refreshDecodedTable();
    resetActionContainer();
    $('#bet-amount-container').hide();
    updatePot();
    showAllCards();
    showWinners();
    populatePlayerData();
    modalClose();
    $('.current-bet span').text('0');
    $('#start-new-game').show();
}

function showAllCards(){
    var betterId;
    var leftCardSrc;
    var rightCardSrc;

    $('.player-container').css('opacity', '1');

    for (var i = 0; i < tableJsonDecoded.players.length; i++) {
        betterId = 'player' + (i + 1);
        leftCardSrc = "/images/" + tableJsonDecoded.players[i].holeCards[0].cardImg;
        rightCardSrc = "/images/" + tableJsonDecoded.players[i].holeCards[1].cardImg;
        $("#" + betterId + " .active-player .cards .left-card").attr('src', leftCardSrc);
        $("#" + betterId + " .active-player .cards .right-card").attr('src', rightCardSrc);
    }
}

function showWinners() {
    var betterId;

    for (var i = 0; i < tableJsonDecoded.players.length; i++) {
        if(tableJsonDecoded.players[i].isWinner){
            betterId = 'player' + (i + 1);
            $("#" + betterId + " .active-player .winner-badge").show();
        }
        tableJsonDecoded.players[i].handValue = null;
        tableJsonDecoded.players[i].holeCards = [];
    }
}

function encodeDecodedTable() {
    tableJsonLocal = JSON.stringify(tableJsonDecoded);
}