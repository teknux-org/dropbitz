${i18n("drop.email.info")}

- ${i18n("drop.email.name")} : ${model.name}
- ${i18n("drop.email.filename.origin")} : ${model.fileName}
<#if model.finalFileName??>
- ${i18n("drop.email.filename.final")} : ${model.finalFileName}
</#if>
<#if model.success>
- ${i18n("drop.email.status")} : ${i18n("drop.email.status.ok")}
<#else>
- ${i18n("drop.email.status")} : ${i18n("drop.email.status.error")}
</#if>
