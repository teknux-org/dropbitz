<script src="/static/js/messages.js" type="text/javascript"></script>
<div id="messages" <#if !model.messages??> class="hidden"</#if>>
    <#if model.messages??>
    	<#foreach message in model.messages>
    		<#--Duplicate html to messages.ftl-->
        	<div id="message-id-${message.id}" class="alert alert-${message.type.cssClass} alert-dismissible" role="alert">
        		<#if message.closable>
        			<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button> 
        		</#if>
        		${message.message}
            </div>
    	</#foreach>
    </#if>
</div>
