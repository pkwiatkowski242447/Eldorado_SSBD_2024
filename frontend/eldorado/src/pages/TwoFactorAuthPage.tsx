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
import {useTranslation} from "react-i18next";
import {toast} from "@/components/ui/use-toast.ts";


function TwoFactorAuthPage() {
    const {t} = useTranslation();

    const FormSchema = z.object({
        pin: z.string().min(8, {
            message: t("twoFactorAuthPage.wrongCode"),
        }),
    })

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
                // console.log(login);
                // console.log(data.pin);
            }).catch((error) => {
                if (error.response && error.response.data) {
                    const {message, violations} = error.response.data;
                    const violationMessages = violations.map((violation: string | string[]) => t(violation)).join(", ");

                    toast({
                        variant: "destructive",
                        title: t(message),
                        description: violationMessages,
                    });
                } else {
                    toast({
                        variant: "destructive",
                        description: "Error",
                    });
                }
                // console.log(error.response ? error.response.data : error);
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
                                <FormLabel>{t("twoFactorAuthPage.title")}</FormLabel>
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
                                    {t("twoFactorAuthPage.info")}
                                </FormDescription>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />

                    <Button type="submit">{t("twoFactorAuthPage.submit")}</Button>
                </form>
            </Form>
        </div>
    )
}

export default TwoFactorAuthPage
