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
        
    };
    var remove = function(obj, callback) {
        
    };
    
    return {
        getAll: getAll,
        add:add,
        update:update,
        remove:remove
    }
})();