<HTML>

<HEAD>
%include ../../WmRoot/pub/b2bStyle.css%

<SCRIPT LANGUAGE="JavaScript">


	function onClick (action) 
	{
		if (action == "reset") 
		{
			document.location="disable_trigger.dsp?action=reset";
		} else if (action == "enable") 
		{
			document.location="disable_trigger.dsp?action=enable";
		} else if (action == "disable") 
		{
			document.location="disable_trigger.dsp?action=disable";
		}
	}


</SCRIPT>
</HEAD>

<BODY>

<TABLE WIDTH=100%>

<div class="position">
<TABLE WIDTH="100%">
	
%ifvar action%
%switch action%
%case 'enable'%	
	%invoke TriggerManager.process:EnableAllTriggers%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%		
%case 'disable'%	
	%invoke TriggerManager.process:DisableAllTriggers%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%
%case 'reset'%	
	%invoke TriggerManager.process:ResetAllTriggers%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%
	
%endswitch%
%endif action%

<TR><TD>

<TABLE WIDTH=100%>

	<TR><TH class="title" colspan=2>Disable/Enable All Triggers in Server</TH></TR>

	<TR><TD class="action" colspan=2>
		<INPUT type="button" value="Disable" onclick="onClick('disable');"></INPUT>
		<INPUT type="button" value="Enable" onclick="onClick('enable');"></INPUT>
		<INPUT type="button" value="Reset" onclick="onClick('reset');"></INPUT>
		</TD></TR>	
</TABLE>
</TD></TR></TABLE>
</FORM>
</BODY>

</HTML>
