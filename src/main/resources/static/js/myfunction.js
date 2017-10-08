/**
 * Created by piotrek on 02.08.17.
 */
Dropzone.options.customDropzone = {
    maxFilesize: 912, // MB
    init: function () {
        this.on("success", function (fileinfo,xhr) {
            $("#message").append($('<div>').addClass("alert alert-success alert-dismissable").append(
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +

                '<strong>Success! </strong>' + xhr.message));
            var filename = xhr.fileInfo.filename;
            var filePath = xhr.fileInfo.filePath;
            var downloadURL = xhr.fileInfo.fullDownloadURL;
            var removeURL = xhr.fileInfo.removeURL;
            $("#files > ul").append (
                $('<li>').attr('id', filename).append(
                    $('<div>').addClass("row").append(
                        $('<div>').addClass("col-md-12").append(
                            $('<a>').attr('href',downloadURL).addClass("text-center file-link").append(
                                filename
                            ),

                            $('<button>').addClass("btn btn-default rel-button del-file").append(
                                $('<span>').addClass("glyphicon glyphicon-remove").attr('onclick',"removeFile('"+removeURL+"','"+filename+"')"))
                            )
                        )
                    )
                );

            console.log(" " + xhr);




        });
        this.on("error", function (file,xhr) {
            $("#message").append($('<div>').addClass("alert alert-danger alert-dismissable").append(
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +

                '<strong>Error! </strong>' + xhr.message));
            $(file.previewElement).find('.dz-error-message').text(xhr.message);
        });
    }

};


function removeFile(fileURL, htmlID) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });


    console.log(fileURL);
    $.ajax({
        method: "DELETE",
        url: fileURL ,
    })
        .done(function( xhr ) {
            $("#message").append($('<div>').addClass("alert alert-success alert-dismissable").append(
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +

                '<strong>Success! </strong>' + xhr.message));
            //var file_html_id ="#"+ filename.replace(/[!"#$%&'()*+,.\/:;<=>?@\[\\\]^`{|}~]/g, "\\\\$&");
            console.log($.escapeSelector(htmlID));
            //$('#'+ $.escapeSelector(filename)).remove();
            $('[id="'+htmlID+'"]').remove()

            return true;
        });
}



function createDirectory(path) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    console.log("" +  $("#directoryNameInput").val());
    var data = {}
    data["dirname"] = $("#directoryNameInput").val();

    console.log(path);

    dirPath = path.replace("uploadfiles", "uploadfiles/directory");

    console.log(dirPath);

    $.ajax({
        method: "POST",
        url:  "/uploadfiles/directory/" + dirPath,
        contentType: "application/json",
        data: JSON.stringify(data),
        dataType: 'json',
        success : function(xhr){
            $("#message").append($('<div>').addClass("alert alert-success alert-dismissable").append(
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +

                '<strong>Success! </strong>' + xhr.message));
            var filename = xhr.fileInfo.filename;
            var filePath = xhr.fileInfo.filePath;
            var removeURL = xhr.fileInfo.removeURL;
            $("#files > ul").append (
                $('<li>').attr('id', filename).append(
                    $('<div>').addClass("row").append(
                        $('<div>').addClass("col-md-12").append(
                            $('<a>').attr('href',xhr.fileInfo.fullDownloadURL).addClass("text-center file-link").append(
                                filename
                            ),

                            $('<button>').addClass("btn btn-default rel-button del-file").append(
                                $('<span>').addClass("glyphicon glyphicon-remove").attr('onclick',"removeFile('"+removeURL+"','"+filename+"')"))
                        )
                    )
                )
            );

            console.log(" " + xhr);
        },
        error : function (xhr, status, error) {
            console.log("xhr: " + xhr + " status: " + status + " error: " + error);
            console.log(xhr);
            $("#message").append($('<div>').addClass("alert alert-danger alert-dismissable").append(
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +

                '<strong>Error! </strong>' + xhr.responseJSON.message));
        }
    });
}
