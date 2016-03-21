<html>

<head>
	<title>Das Pre-Worker-Interface</title>
	<link rel="stylesheet" href="/stylescreative.css"> </head>

<body>
	<div id="afgstellung">
		<form id="calibanswer" action="/assignment/${expid}/calibration"
		method="POST" name="calibanswerform">
		
		<h1>${exdesc}</h1>
		

					<select name = "answer">
						<#list calibdata as cd>						
							<option <#if cd = (answer)!>selected</#if>>${cd}</option>
						</#list>
                                      </select>
			<p>
          <input type="submit">
	</div>
</body>

</html>
