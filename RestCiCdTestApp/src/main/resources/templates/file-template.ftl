Hi ${userName}!

How are you?


These are the cars available now!!

<#list cars as car>
${car.name?right_pad(20)}: ${car.price}
</#list> 