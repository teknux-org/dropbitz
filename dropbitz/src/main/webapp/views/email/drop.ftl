<#import "../lib/utils.ftl" as utils>
<html>
	<body>
		<div>
			<table cellspacing="0" cellpadding="8" border="0" style="width:100%;font-family:Arial,Sans-serif;border:1px Solid #ccc;border-width:1px 2px 2px 1px;background-color:#fff;">
				<tr>
					<td style="background-color:#f6f6f6;color:#888;border-top:1pxSolid #ccc;font-family:Arial,Sans-serif;font-size:13px;">
						<h3 style="padding:0 0 6px 0;margin:0;font-family:Helvetica,Normal;font-size:16px;font-weight:bold;color:#222">
							<span>DropBitz</span>
						</h3>
						<p>
							${i18n(i18nKey.DROP_EMAIL_INFO)}
						</p>
					</td>
				</tr>
				<tr>
					<td>
						<div style="padding:2px">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <@entry name="${i18n(i18nKey.DROP_EMAIL_NAME)}" value="${model.name}" />
								<#if model.email??>
                                    <@entry name="${i18n(i18nKey.DROP_EMAIL_EMAIL)}" value="${model.email}" />
								</#if>
                                <@entry name="${i18n(i18nKey.DROP_EMAIL_FILENAME_ORIGIN)}" value="${model.fileName}" />
								<#if model.finalFileName??>
                                    <@entry name="${i18n(i18nKey.DROP_EMAIL_FILENAME_FINAL)}" value="${model.finalFileName}" />
								</#if>
                                <#if model.success>
                                    <#global fileSize>
                                        <@utils.readableFileSize size=model.fileLength/>
                                    </#global>
                                    <@entry name="${i18n(i18nKey.DROP_EMAIL_FILE_LENGHT)}" value="${fileSize}" />
                                </#if>
                                <#if model.success>
                                    <@entry name="${i18n(i18nKey.DROP_EMAIL_STATUS)}" value="${i18n(i18nKey.DROP_EMAIL_STATUS_OK)}" color="#2CA255"/>
                                <#else>
                                    <@entry name="${i18n(i18nKey.DROP_EMAIL_STATUS)}" value="${i18n(i18nKey.DROP_EMAIL_STATUS_ERROR)}" color="#E92F21"/>
                                </#if>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>

<#macro entry name value color="#222">
<tr>
    <td style="padding:0 1em 10px 0; font-family:Arial,Sans-serif;font-size:13px;color:#666" valign="top">
        ${name}
    </td>
    <td style="padding-bottom:10px;font-family:Arial,Sans-serif;font-size:13px;color:${color}" valign="top">
        ${value}
    </td>
</tr>
</#macro>