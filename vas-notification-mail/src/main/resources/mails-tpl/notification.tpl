Hy {{user.username}}, 

You have subscribed to Vas notifications about your address {{address.label}}.

These are the stations which match your requirement, so at {{notification.hour}}:{{notification.min}}.

Autolibs:

{{#stations.autolibs.stations}}
	{{id}}
	> Tiers: {{tiers}}
	> Abri: {{abri}}
	> Autolib: {{autolib}}
	-----
{{/stations.autolibs.stations}}

Velibs:

{{#stations.velibs.stations}}
	{{id}}
	> Distance: {{dist}}
	> Stands: {{stands}}
	> Available: {{available}}
	> Available stands: {{availableStands}}
{{/stations.velibs.stations}}

Regards,

Vas notifier