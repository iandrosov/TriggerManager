<HTML>

<HEAD>
%include ../../WmRoot/pub/b2bStyle.css%

<SCRIPT LANGUAGE="JavaScript">


	function onClick (action) {
		if (action == "reset") {
			document.location="manage_trigger.dsp?action=reset";
		} else if (action == "submit") {
			document.changeform.submit();
		}
	}


</SCRIPT>
</HEAD>

<BODY>

<TABLE WIDTH=100%>

<TR><TD>
<FORM name="changeform" action="list_trigger_package.dsp" method="POST">

<TABLE WIDTH=100%>

	<TR><TH class="title" colspan=2>Manage Triggers Based in Package</TH></TR>

	<TR><TD class="action" colspan=2>
		<INPUT type="button" value="Submit" onclick="onClick('submit');"></INPUT>
		</TD></TR>
	<TR ><TH colspan=2 class="message">WARNING! Some actions in this section will require reload of selected package.
	</TH></TR>
	<TR ><TH colspan=2 class="heading">Select a Package for Trigger Location</TH></TR>

	<TR><TH class="oddcol" width=28%>Package</TH>
	<TD class="oddrow-l">
	%invoke wm.server.packages:packageList%
	%ifvar packages%
			<SELECT name="package">
			%loop packages%
			%ifvar enabled equals('true')%  
				<OPTION value="%value name%">%value name%</OPTION>
			%endif%
			%endloop%
			</SELECT>
	%else%
		<B><I>Fatal Error. No packages!</I></B>
	%endif%
	%endinvoke%
	</TD>
	</TR>

</TABLE>
</TD></TR></TABLE>
</FORM>
</BODY>

</HTML>
