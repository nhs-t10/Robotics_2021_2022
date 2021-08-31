

input(leftJoystickY) -> (in*-1) --> omniDrive() => (in.0, in.1, in.2, in.3*-1) => output(motors.fl, motors.fr, motors.br, motors.bl)
                                    ^   ^                                    !
                                    |   |                                    !
                                    |   input(rightJoystickX)                !
                                    |                                        !
                                    |                                        !
                                    |                                        !
                                   input(leftJoystickX)                      !
                                                                             !          input(a)
                                                                             v            v
                                                                            scale(0.2) => if() => output(motors.fl, motors.fr, motors.br, motors.bl)

            TelemetryManager(all) -> output()