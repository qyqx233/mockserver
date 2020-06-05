<html>
	<body>

		<table>
				<#list data.items as item>
				    <tr>
                        <td>${item.first}</td>
                        <td>${item.second}</td>
        			</tr>
        		</#list>
		</table>

	</body>
</html>