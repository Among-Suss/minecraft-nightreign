execute as @a[tag=nightreign.c.wylder,tag=!nightreign.e.wylder.grappled] if score @s nightreign.parry matches 1 run function nightreign:wylder/tick/grapple_apply
execute as @a[tag=nightreign.e.wylder.grappled] run scoreboard players add @s nightreign.wylder.grapple_cooldown 1
execute as @a if score @s nightreign.wylder.grapple_cooldown matches 10.. run tag @s remove nightreign.e.wylder.grappled