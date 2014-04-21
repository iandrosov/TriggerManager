<HTML>

<HEAD>
%include ../../WmRoot/pub/b2bStyle.css%

<SCRIPT LANGUAGE="JavaScript">


	function onClick (action) 
	{
		if (action == "reset") 
		{
			document.location="stop_trigger.dsp?action=reset";
		} else if (action == "stop") 
		{
			document.location="stop_trigger.dsp?action=stop";
		} else if (action == "start") 
		{
			document.location="stop_trigger.dsp?action=start";
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
%case 'start'%	
	%invoke TriggerManager.process:StartPollingAllTriggers%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%		
%case 'stop'%	
	%invoke TriggerManager.process:StopPollingAllTriggers%
	<TR><TH id="message" colspan=8>%value message%</TH></TR>
	%endinvoke%
%endswitch%
%endif action%

<TR><TD>

<TABLE WIDTH=100%>
<TR ><TH colspan=2 class="message">
This activity is only available in webMethods IS Version 6.0.1. It will not work
in prior versions.
</TH></TR>

	<TR><TH class="title" colspan=2>Stop/Start Polling All Triggers in Server</TH></TR>

	<TR><TD class="action" colspan=2>
		<INPUT type="button" value="Stop" onclick="onClick('stop');"></INPUT>
		<INPUT type="button" value="Start" onclick="onClick('start');"></INPUT>		
		</TD></TR>	
</TABLE>
</TD></TR></TABLE>
</FORM>
</BODY>

</HTML>
