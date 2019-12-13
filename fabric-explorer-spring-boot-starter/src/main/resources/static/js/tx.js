const basePath = $('#baseURL').val() + '/' + $('#path').val() + '/';

const txid = $('#txid').val();
const view = $('#view').val();
const viewHistory = $('#viewHistory').val();
const viewDetails = $('#viewDetails').val();
$(document).ready(function() {
	queryBlockInfo();
	initDataTable('readTable');
	initDataTable('writeTable');
	queryRWSet();

	$('#query').bind('keypress keydown keyup', function(e) {
		if (e.keyCode == 13) {
			e.preventDefault();
			performSearch();
		}
	});

	$('[data-toggle="tooltip"]').tooltip()
});

function queryBlockInfo() {
	$('#txIdDisplay').html(txid);
	$.ajax({
		url : basePath + "query/tx/" + txid,
	}).then(function success(res) {
		if (res.status > 0) {
			$('#txDateDisplay').html(format(res.data.date));
			$('#txCreatorDisplay').html(res.data.creator);

			$('[data-toggle="tooltip"]').tooltip()
		}
	}, function fail(data, status) {

	});
}

function queryRWSet() {
	$.ajax({
		url : basePath + "query/txrw/" + txid,
	}).then(function success(res) {
		if (res.status > 0) {
			var readTable = $('#readTable').DataTable();
			readTable.rows.add(res.data.reads);
			readTable.draw();

			var writeTable = $('#writeTable').DataTable();
			writeTable.rows.add(res.data.writes);
			writeTable.draw();

			$('[data-toggle="tooltip"]').tooltip()
		}
	}, function fail(data, status) {

	});
}

function initDataTable(id) {
	var table = $('#' + id)
			.DataTable(
					{
						"paging" : false,
						"ordering" : false,
						"info" : false,
						"searching" : false,
						"processing" : true,
						"serverSide" : false,
						"columns" : [
								{
									"name" : 'index',
									"data" : function(row) {
										return row.index + 1;
									}
								},
								{
									"name" : "key",
									"data" : function(row) {
										var html = '<form id="' + row.key + "-"
												+ row.index
												+ '" action="history?key='
												+ row.key + '" method="post">';
										html += '<a href="javascript:;" onclick="document.getElementById(\''
												+ row.key
												+ "-"
												+ row.index
												+ '\').submit();" data-toggle="tooltip" title="'
												+ viewHistory + '">' + row.key
										'</a>';
										html += '<input type="hidden" name="type" value="'
												+ row.type + '"/>';
										html += '</form>';
										return html;
									}
								},
								{
									"name" : "value",
									"data" : function(row) {
										var id = 'value_' + row.key + '_'
												+ row.index;
										var html = '<div>';
										html += '<a data-toggle="collapse" href="#'
												+ id
												+ '" role="button" aria-expanded="false" aria-controls="'
												+ id
												+ '" data-toggle="tooltip" title="'
												+ viewDetails
												+ '">'
												+ view
												+ '</a>';
										html += '	<div class="collapse multi-collapse" id="'
												+ id + '">';
										html += '  <div class="card card-body" style="overflow-y: auto; height:350px;" >';
										html += formatJson(row.value);
										html += ' </div>';
										html += '</div>';
										html += '</div>';

										return html;
									}
								}, {
									"name" : "remarks",
									"data" : function(row) {
										if (row.remarks == 'true') {
											return 'yes';
										}
										return '';
									}
								} ]
					});
};

function formatJson(value) {
	var result = FormatJSON(JSON.parse(value));
	return '<pre><code>' + result + '</code></pre>';
}

function performSearch() {
	var query = $('#query').val();
	if (isNaN(query)) {
		window.location.href = basePath + 'tx?txid=' + query;
	} else {
		window.location.href = basePath + 'block?height=' + query;
	}
}
