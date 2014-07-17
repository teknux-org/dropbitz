${i18n(i18nKey.DROP_EMAIL_INFO)}

- ${i18n(i18nKey.DROP_EMAIL_NAME)} : ${model.name}
<#if model.email??>
- ${i18n(i18nKey.DROP_EMAIL_EMAIL)} : ${model.email}
</#if>
- ${i18n(i18nKey.DROP_EMAIL_FILENAME_ORIGIN)} : ${model.fileName}
<#if model.finalFileName??>
- ${i18n(i18nKey.DROP_EMAIL_FILENAME_FINAL)} : ${model.finalFileName}
</#if>
<#if model.success>
- ${i18n(i18nKey.DROP_EMAIL_STATUS)} : ${i18n(i18nKey.DROP_EMAIL_STATUS_OK)}
<#else>
- ${i18n(i18nKey.DROP_EMAIL_STATUS)} : ${i18n(i18nKey.DROP_EMAIL_STATUS_ERROR)}
</#if>
