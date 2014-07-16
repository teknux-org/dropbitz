<footer class="text-center">
    <span>
    	<#assign applicationProperties = statics["org.teknux.dropbitz.util.ApplicationProperties"]>  
		${i18n(i18nKey.FOOTER_POWEREDBY)} <a class="brand-font" href="https://github.com/teknux-org/dropbitz">DropBitz</a> v${applicationProperties.getInstance().getProperty(applicationProperties.APPLICATION_VERSION_KEY)}
    </span>
</footer>
