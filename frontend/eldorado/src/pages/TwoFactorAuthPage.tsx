"use client"

import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"
import eldoLogo from "../assets/eldorado.png";
import {Button} from "@/components/ui/button"
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,} from "@/components/ui/form"
import {InputOTP, InputOTPGroup, InputOTPSlot,} from "@/components/ui/input-otp"
import {useAccount} from "@/hooks/useAccount.ts";
import {useParams} from "react-router-dom";

const FormSchema = z.object({
    pin: z.string().min(8, {
        message: "Your one-time password must be 8 characters.",
    }),
})

function TwoFactorAuthPage() {
    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            pin: "",
        },
    })

    const {login} = useParams();

    const {logIn2fa} = useAccount();

    function onSubmit(data: z.infer<typeof FormSchema>) {
        if (login) {
            logIn2fa(login, data.pin).then(() => {
                console.log(login);
                console.log(data.pin);
            }).catch((error) => {
                console.log(error);
            });
        }
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2 justify-center"/>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className=" w-2/3 space-y-6 justify-center flex mx-auto">
                    <FormField
                        control={form.control}
                        name="pin"
                        render={({field}) => (
                            <FormItem>
                                <FormLabel>One-Time Password</FormLabel>
                                <FormControl>
                                    <InputOTP maxLength={8} {...field}>
                                        <InputOTPGroup>
                                            <InputOTPSlot index={0}/>
                                            <InputOTPSlot index={1}/>
                                            <InputOTPSlot index={2}/>
                                            <InputOTPSlot index={3}/>
                                            <InputOTPSlot index={4}/>
                                            <InputOTPSlot index={5}/>
                                            <InputOTPSlot index={6}/>
                                            <InputOTPSlot index={7}/>
                                        </InputOTPGroup>
                                    </InputOTP>
                                </FormControl>
                                <FormDescription>
                                    Please enter the one-time password sent to your e-mail address.
                                </FormDescription>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />

                    <Button type="submit">Submit</Button>
                </form>
            </Form>
        </div>
    )
}

export default TwoFactorAuthPage
