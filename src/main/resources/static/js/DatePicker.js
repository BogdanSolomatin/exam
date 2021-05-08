// $(function () {
//     $("#startDate").datepicker({
//         minDate: 0,
//         onSelect: function(selectedDate) {
//             let temp = new Date(selectedDate);
//             temp.setDate(temp.getDate() + 1);
//             $("#endDate").datepicker("option", "minDate", temp);
//             $(this).attr('value', $('#startDate').val());
//         }
//     });
//
//     $("#endDate").datepicker({
//        minDate: 0,
//        onSelect: function(selectedDate) {
//            $("#startDate").datepicker("option", "maxDate", selectedDate)
//            $(this).attr('value', $('#endDate').val());
//        }
//     });
// })
let reservations = new Array();

$("#btn").on('click', function () {
    sendRequest("GET", 'bookings')
        .then((data) => tester(JSON.parse(data)))
})


function tester(data) {
    reservations = new Array();
    for (let i = 0; i < data.length; i++) {
        let arr = new Array(data[i].checkIn, data[i].checkOut);
        reservations.push(arr);
    }
}

$(function () {
    $("#from").datepicker({
        //dateFormat: "yyyy-mm-dd",
        beforeShowDay: function (date) {
            for (let i = 0; i < reservations.length; i++) {
                let from = new Date(reservations[i][0]);
                let to = new Date(reservations[i][1]);
                date.setHours(0, 0, 0, 0);
                from.setHours(0, 0, 0, 0);
                to.setHours(0, 0, 0, 0);
                if (date >= from.getTime() && date <= to) return false;
            }
            return [reservations.indexOf(date) == -1];
        }
    })

    $("#to").datepicker({
        //dateFormat: "yyyy-mm-dd",
        beforeShowDay: function (date) {
            for (let i = 0; i < reservations.length; i++) {
                let from = new Date(reservations[i][0]);
                let to = new Date(reservations[i][1]);
                date.setHours(0, 0, 0, 0);
                from.setHours(0, 0, 0, 0);
                to.setHours(0, 0, 0, 0);
                if (date >= from.getTime() && date <= to) return false;
            }
            return [reservations.indexOf(date) == -1];
        },
        onSelect: function (date) {
            let flag = true;
            let fromDate = $("#from").datepicker('getDate');
            let toDate = $("#to").datepicker('getDate');
            for (let i = new Date(fromDate); i <= toDate; i.setDate(i.getDate() + 1)) {
                for(let j = 0; j < reservations.length; j++) {
                    if(reservations[j][0].indexOf(jQuery.datepicker.formatDate('yy-mm-dd', i)) != -1 ||
                        reservations[j][1].indexOf(jQuery.datepicker.formatDate('yy-mm-dd', i)) != -1) {
                        flag = false;
                    }
                }
            }
            if (!flag) {
                $($("#to").val(""));
                alert("Test");
            }
        }
    })
})
$('#checkDate').datepicker({
    //dateFormat: 'yyyy-mm-dd',
    beforeShowDay: function (date) {
        for (let i = 0; i < reservations.length; i++) {
            let from = new Date(reservations[i][0]);
            let to = new Date(reservations[i][1]);
            date.setHours(0, 0, 0, 0);
            from.setHours(0, 0, 0, 0);
            to.setHours(0, 0, 0, 0);
            if (date >= from.getTime() && date <= to) return false;
        }
        return [reservations.indexOf(date) == -1];
    }
});