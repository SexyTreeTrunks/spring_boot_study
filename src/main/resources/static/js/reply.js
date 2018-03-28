var replyManager = (function() {
    var getAll = function(obj, callback) {
        console.log("get all...");
        $.getJSON('/replies/' + obj, callback);
    };
    var add = function(obj, callback) {
        $.ajax({
            type:'post',
            url:'/replies/' + obj.bno,
            data:JSON.stringify(obj),
            dataType:'json',
            contentType:"application/json",
            success:callback
        });
    };
    var update = function(obj, callback) {
        $.ajax({
            type:'put',
            url:'/replies/' + obj.bno,
            data:JSON.stringify(obj),
            contentType:"application/json",
            success:callback
        });
    };
    
    var remove = function(obj, callback) {
        $.ajax({
            type:'delete',
            url:'/replies/' + obj.bno + '/' + obj.rno,
            data:JSON.stringify(obj),
            contentType:"application/json",
            success:callback
        });
    };
    
    return {
        getAll: getAll,
        add:add,
        update:update,
        remove:remove
    }
})();

var replyEvent = (function() {
    var onDelete = function(rno, bno) {
        if(!confirm("삭제하시겠어여??")) {
            return;
        }
        var obj = {bno:bno, rno:rno};
        replyManager.remove(obj, function(rno) {
            console.log("rno: " + rno + " reply removed");
            $("#reply-list").find("#"+rno).remove();
            alert("댓글이 삭제되었습니다");
        });
    };

    var onModify = function(rno, bno) {
        console.log("modify called");
        var origin_str = $("#"+rno).find(".reply-content").html();
        var new_str = '<div><div>'
                + '<textarea class="form-control" rows="3" name="reply-text"></textarea>'
                + '</div><div>'
                + '<button type="button" class="btn btn-primary" id="saveModify">Save</button>'
                + '<button type="button" class="btn btn-danger" id="cancelModify">Cancel</button>'
                + '</div></div>';
        
        $("#"+rno).find(".reply-content").html(new_str);

        $("#saveModify").on('click', function(){
            var reply = $("textarea[name='reply-text']").val();
            if(isNull(reply)) {
                alert("내용을 입력해주세여");
                return;
            }

            var obj = {reply:reply, rno:rno, bno:bno};
            replyManager.update(obj, function() {
                $("#"+rno).find(".reply-content").html(origin_str);
                $("#"+rno).find(".reply-text").text(reply);
                alert("댓글이 변경되었습니다");
            });
        });

        $("#cancelModify").on('click', function() {
            $("#"+rno).find(".reply-content").html(origin_str);
        });
    };

    var onAdd = function(bno) {
        if(!confirm("추가하시겠어여??"))
            return;
        var replyObj = $("textarea[name='newReply']");
        var replyerObj = $("input[name='newReplyer']");
        var reply = replyObj.val();
        var replyer = replyerObj.val();
        if(isNull(reply)||isNull(replyer)) {
            alert("내용을 입력해주세여");
            return;
        }

        var obj = {reply:reply, replyer:replyer, bno:bno};
        replyManager.add(obj, function(reply) {
            var str= toReplyListItemStr(reply);
            $("#reply-list").prepend(str);
            var rno = reply.rno;
        });
        replyObj.val("");
        replyerObj.val("");
    }

    return {
        onDelete:onDelete,
        onModify:onModify,
        onAdd:onAdd
    }
})();

function toReplyListItemStr(reply, bno) {
    var str = '<div class="list-group-item reply-block" id="'+ reply.rno + '">'
        + '<small><span style="font-weight:bold">' + reply.replyer + '</span></small>'
        + '<div class="reply-content">'
        + '<div class="reply-date">' + reply.regdate + '</div>'
        + '<div class="reply-text">' + reply.reply + '</div>'
        + '<div class="bottom-reply">'
        + '<ul class="reply-actions">'
        + '<li class="modifyReplyBtn" onclick="javascript:replyEvent.onModify('+ reply.rno +','+ bno + ')"> modify </li>'
        + '<li class="deleteReplyBtn" onclick="javascript:replyEvent.onDelete('+ reply.rno +','+ bno + ')"> delete </li>'
        + '</ul>' + '</div>' + '</div>' + '</div>';
    return str;
}

function isNull(str) {
    if(str == "")
        return true;
    else
        return false;
}
