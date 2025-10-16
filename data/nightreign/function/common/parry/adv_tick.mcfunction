advancement revoke @s only nightreign:parry_tick
scoreboard players add @s nightreign.parry 1
execute store result score @s nightreign.parry.timestamp run time query gametime
scoreboard players add @s nightreign.parry.timestamp 2
schedule function nightreign:common/parry/stop 2t append