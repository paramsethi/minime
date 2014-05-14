<html>
<head>
<title>URL Shortener - Min</title>
<style>
#minimeForm {
	position: absolute;
	margin-top: 20px;
	margin-left: 20px;
	font-family: inherit;
	font-style: normal;
	text-align: left;
}
</style>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript">
	function init() {
		jQuery(document).on('submit', 'form.minimeForm', function() {
			jQuery.ajax({
				url : jQuery(this).apiSelect.name,
				type : jQuery(this).attr('method'),
				data : jQuery(this).serialize(),
				success : function(data) {
					jQuery("#response").innerHtml(data);
				},
				error : function(xhr, err) {
					alert('Error');
				}
			});
			return false;
		});
	}

	function onSelectChange(selectElem) {
		var selected = jQuery(selectElem).val();

		if (selected === "unmin") {
			jQuery("#minUnminBlock").show();
			jQuery("#minBlock").hide();
		} else if (selected === "min") {
			jQuery("#minUnminBlock").show();
			jQuery("#minBlock").show();
		} else {
			jQuery("#minUnminBlock").hide();
			jQuery("#minBlock").hide();

		}
	}
</script>
</head>
<body onload="JavaScript:init();">
	<h1>Minime!</h1>
	<div id="minimeForm">

		<form id="minimeForm" method="post">
			<table style="width: 400px">
				<tr>
					<td>Select API call to run:</td>
					<td><select id="apiSelect"
						onchange="JavaScript:onSelectChange(this);"><option
								id="min" name="min">min</option>
							<option id="unmin" name="unmin">unmin</option>
							<option id="stats" name="stats">stats</option></select></td>
				</tr>
				<div id="minUnminBlock">
					<div id="minBlock">
						<tr>
							<td>Enter a long boring URL:</td>
							<td><input type="text" name="origurl" /></td>
						</tr>
					</div>
				<tr>
					<td>Custom Alias (optional for min): http://mini.me/</td>
					<td><input type="text" name="customalias" maxlength="10" /></td>
				</tr>
				</div>
				<tr style="padding-top: 20px;">
					<td align="center" width="100%"><input style="align: center;"
						type="submit" name="MiniMe!" /></td>
				</tr>

			</table>
		</form>
	</div>

	<div id="response"></div>
</body>
</html>