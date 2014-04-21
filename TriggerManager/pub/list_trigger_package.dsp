<HTML>
<HEAD>
<TITLE>TRigger List</TITLE>
%include ../../WmRoot/pub/b2bStyle.css%
<SCRIPT LANGUAGE="JavaScript">


	function confirmEnable (pkg,trg) {
		var s1 = "Is it OK to enable or restart this trigger - " + trg +" ?\n\n";
		return confirm (s1);
	}

	function confirmDisable (pkg,trg) {
		var s1 = "Is it OK to stop this trigger - " + trg + " ?\n\nThis will automatically reload the package to stop trigger.";
		return confirm (s1);
	}


</SCRIPT>
</HEAD>
<BODY>


<div class="position">
<TABLE WIDTH="100%">
%ifvar action%
%switch action%
%case 'enable'%

	%invoke TriggerManager.process:StartTrigger%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%		
%case 'disable'%
	%invoke TriggerManager.process:StopTrigger%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%
%endswitch%

%endif action%

%invoke TriggerManager.util:getTriggersFromPackage%

<TR><TH class="title" colspan=4>Trigger List in Package - %value package%</TH></TR>
<TR ><TH colspan=2 class="message">
To STOP a selected trigger will automatically reload selected package.
To START a selected trigger DOES NOT require a reload of a package.
Use STOP/START All Triggers menu to avoid package reload and preserve all client queues.
</TH></TR>
	
	<TR class="heading">
		<TH width=20%>Trigger Name</TH>
		<TH width=10%>Enabled</TH>
		</TR>

	%loop trigger_info%
	<TR>
		<TD class="rowdata">
			&nbsp;%value trigger_name%
		</TD>
		<TD class="rowdata">
			%ifvar status equals('true')%
				<A HREF="list_trigger_package.dsp?action=disable&package=%value package%&status=false&triggername=%value trigger_name%"
					ONCLICK="return confirmDisable('%value package%','%value trigger_name%');">	
				<IMG class="alone" SRC="/WmRoot/icons/green-ball.gif" border="no" alt="[Enable]"></A>
			%else%
				<A HREF="list_trigger_package.dsp?action=enable&package=%value package%&status=true&triggername=%value trigger_name%"
					ONCLICK="return confirmEnable('%value package%','%value trigger_name%');">
				<IMG class="alone" SRC="/WmRoot/icons/red-ball.gif" border="no" alt="[Disable]"></A>
			%endif%
			</TD>

		</TR>
	%endloop%

</TABLE>
%onerror%
	<P> Error occured</P>
%endinvoke%

</div></BODY>

</HTML>

