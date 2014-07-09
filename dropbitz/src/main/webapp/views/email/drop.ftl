<html>
	<body>
		<div>
			<table cellspacing="0" cellpadding="8" border="0" style="width:100%;font-family:Arial,Sans-serif;border:1px Solid #ccc;border-width:1px 2px 2px 1px;background-color:#fff;">
				<tr>
					<td style="background-color:#f6f6f6;color:#888;border-top:1pxSolid #ccc;font-family:Arial,Sans-serif;font-size:13px">
						<h3 style="padding:0 0 6px 0;margin:0;font-family:Arial,Sans-serif;font-size:16px;font-weight:bold;color:#222">
							<span>DropBitz</span>
						</h3>
						<p>
							${i18n("drop.email.info")}
						</p>
					</td>
				</tr>
				<tr>
					<td>
						<div style="padding:2px">
							<table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td style="padding:0 1em 10px 0; font-family:Arial,Sans-serif;font-size:13px;color:#666" valign="top">
										${i18n("drop.email.name")}
									</td>
									<td style="padding-bottom:10px;font-family:Arial,Sans-serif;font-size:13px;color:#222" valign="top">
										${model.name}
									</td>
								</tr>
								<tr>
									<td style="padding:0 1em 10px 0; font-family:Arial,Sans-serif;font-size:13px;color:#666" valign="top">
										${i18n("drop.email.filename.origin")}
									</td>
									<td style="padding-bottom:10px;font-family:Arial,Sans-serif;font-size:13px;color:#222" valign="top">
										${model.fileName}
									</td>
								</tr>
								<#if model.finalFileName??>
									<tr>
										<td style="padding:0 1em 10px 0; font-family:Arial,Sans-serif;font-size:13px;color:#666" valign="top">
											${i18n("drop.email.filename.final")}
										</td>
										<td style="padding-bottom:10px;font-family:Arial,Sans-serif;font-size:13px;color:#222" valign="top">
											${model.finalFileName}
										</td>
									</tr>
								</#if>
								<tr>
									<td style="padding:0 1em 10px 0; font-family:Arial,Sans-serif;font-size:13px;color:#666" valign="top">
										${i18n("drop.email.status")}
									</td>
									<#if model.success>
										<td style="padding-bottom:10px;font-family:Arial,Sans-serif;font-size:13px;color:#2CA255;font-weight:bold" valign="top">
											${i18n("drop.email.status.ok")}
										</td>
									<#else>
										<td style="padding-bottom:10px;font-family:Arial,Sans-serif;font-size:13px;color:#E92F21;font-weight:bold" valign="top">
											${i18n("drop.email.status.error")}
										</td>
									</#if>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
