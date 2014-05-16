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
<script
	src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
<script type="text/javascript">
	function init() {
		jQuery("#enterOrigUrl").hide();
		jQuery("#enterOrigUrlText").hide();
		jQuery("#enterShortUrl").hide();
		jQuery("#enterShortUrlText").hide();
		jQuery("#submitButton").hide();

	}

	function onSelectChange(selectElem) {
		var selected = jQuery(selectElem).val();
		if (selected == "min") {
			jQuery("#enterShortUrl").hide();
			jQuery("#enterShortUrlText").hide();
			jQuery("#enterOrigUrl").show();
			jQuery("#enterOrigUrlText").show();
		} else if (selected == "unmin") {
			jQuery("#enterShortUrl").show();
			jQuery("#enterShortUrlText").show();
			jQuery("#enterOrigUrl").hide();
			jQuery("#enterOrigUrlText").hide();
		} else {
			jQuery("#enterOrigUrl").hide();
			jQuery("#enterOrigUrlText").hide();
			jQuery("#enterShortUrl").hide();
			jQuery("#enterShortUrlText").hide();
		}
		jQuery("#submitButton").show();

	}

	function submitForm() {

		var postData = jQuery('#minimeForm').serialize();
		var formURL = jQuery("#apiSelect").val();
		jQuery.ajax({
			url : formURL,
			type : "POST",
			data : postData,
			success : function(data, textStatus, jqXHR) {
				jQuery("#response").text(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(errorThrown)
			}
		});

		return false;
	}
</script>
</head>
<body onload="JavaScript:init();">
	<h1>Minime!</h1>
	<div id="minimeFormDiv">

		<form id="minimeForm" method="post">
			<table style="width: 400px">
				<tr>
					<td>Select API call to run:</td>
					<td><select id="apiSelect"
						onchange="JavaScript:onSelectChange(this);">
							<option id="none" value="selectOperation">Select
								Operation</option>
							<option id="min" value="min">Long to Short URL (/min)</option>
							<option id="unmin" value="unmin">Short to Long URL
								(/unmin)</option>
							<option id="stats" value="stats">Application Statistics
								(/stats)</option>
					</select></td>
				</tr>

				<tr>
					<td><div id="enterOrigUrl">Enter Original URL:</div></td>
					<td><div id="enterOrigUrlText">
							<input type="text" name="origurl" />
						</div></td>
				</tr>

				<tr>
					<td><div id="enterShortUrl">Enter Short URL:</div></td>
					<td><div id="enterShortUrlText">
							<input type="text" name="shorturl" />
						</div></td>
				</tr>
			</table>
			<div style="padding: 20px; margin-left: 120px;">
				<input id="submitButton" style="display: inline-block;"
					type="button" value="Submit" name="MiniMe!"
					onclick="JavaScript:submitForm();">
			</div>
		</form>
	</div>
	<pre class="prettyprint lang-xml">
	<div id="response" style="margin-top: 150px"></div>
</pre>
</body>
</html>