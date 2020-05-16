const url = "http://sudokuresolver-env.eba-iagrn2x2.us-east-2.elasticbeanstalk.com";
function getSudoku() {
    var sudoku = "rows=";
    var item = 0;
    $(".cell").each(function () {
        if ($(this).val() !== "") {
            sudoku += $(this).val();
        } else {
            sudoku += "0";
        }
        item++;
        if (item % 9 !== 0) {
            sudoku += ",";
        } else if (item !== 81) {
            sudoku += "&rows=";
        }
    });
    return sudoku;
}

function getNextStep() {
    var sudoku = getSudoku();
    $.ajax({
        type: "GET",
        contentType : "application/json",
        data: sudoku,
        url: url + "/board/next-step",
        success: function(data) {
            if (data !== "") {
                $("#nextStepResult").text(`Number ${data.digit} at position (${data.i+1};${data.j+1})`);
            } else {
                $("#nextStepResult").text("Next logic step is not available for this board");
            }
        },
        error: function(data) {
            console.log(data);
        }
    });
}

function isValid() {
    var sudoku = getSudoku();
    $.ajax({
        type: "GET",
        contentType : "application/json",
        data: sudoku,
        url: url + "/board/valid",
        success: function(data) {
            $("#isValidResult").text(data.toString());
        },
        error: function(data) {
            console.log(data);
        }
    });
}