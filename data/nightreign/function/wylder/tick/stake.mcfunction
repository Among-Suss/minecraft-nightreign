# Resist
execute as @a[scores={nightreign.wylder.stake_resis=1..}] run effect give @s[tag=nightreign.c.wylder] minecraft:resistance 1 5
execute as @a[scores={nightreign.wylder.stake_resis=1..}] run effect give @s[tag=nightreign.c.wylder] minecraft:fire_resistance 10 0

execute as @a[scores={nightreign.wylder.stake_resis=1..}] at @s run summon minecraft:tnt ^ ^ ^ {fuse:0}
execute as @a[scores={nightreign.wylder.stake_resis=1..}] at @s run summon minecraft:tnt ^3 ^ ^2 {fuse:0}
execute as @a[scores={nightreign.wylder.stake_resis=1..}] at @s run summon minecraft:tnt ^-3 ^ ^2 {fuse:0}


execute as @a[scores={nightreign.wylder.stake_resis=1..}] run scoreboard players reset @s nightreign.wylder.stake_resis


# FX
tag @e[type=arrow,nbt={HasBeenShot:1b,item:{components:{"minecraft:potion_contents":{custom_effects:[{duration:2,show_icon:1b,amplifier:2b,id:"minecraft:haste"}]}}}}] add nightreign.e.stake
# #execute as @e[tag=nightreign.e.stake] at @s run effect give @p[tag=nightreign.c.wylder] minecraft:resistance 1 5
# execute as @e[tag=nightreign.e.stake] at @s run summon minecraft:tnt ^3 ^3 ^3 {fuse:0}
execute as @e[tag=nightreign.e.stake] at @s run fill ~-3 ~-3 ~-3 ~1 ~ ~1 fire keep
execute as @e[tag=nightreign.e.stake] at @s run particle minecraft:flame ~-1 ~-1 ~-1 2 2 2 0 50
execute as @e[tag=nightreign.e.stake] at @s run particle minecraft:large_smoke ~-1.5 ~-1.5 ~-1.5 3 3 3 0 50
kill @e[tag=nightreign.e.stake]