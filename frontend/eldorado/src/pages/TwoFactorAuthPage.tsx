import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form";
import {z} from "zod";
import eldoLogo from "../assets/eldorado.png";
import {Button} from "@/components/ui/button";
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {InputOTP, InputOTPGroup, InputOTPSlot} from "@/components/ui/input-otp";
import {useAccount} from "@/hooks/useAccount.ts";
import {useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Loader2} from "lucide-react";
import {useState} from "react";

function TwoFactorAuthPage() {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);

    const FormSchema = z.object({
        pin: z.string().min(8, {
            message: t("twoFactorAuthPage.wrongCode"),
        }),
    });

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            pin: "",
        },
    });

    const {login} = useParams();
    const {logIn2fa} = useAccount();

    function onSubmit(data: z.infer<typeof FormSchema>) {
        if (login) {
            setIsLoading(true);
            logIn2fa(login, data.pin).then(() => {
            }).catch((error) => {
                handleApiError(error);
            }).finally(() => {
                setIsLoading(false);
            });
        }
    }

    return (
        <div className="flex flex-col items-center justify-center">
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="h-auto w-1/2 mb-8"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 w-auto">
                    <FormField
                        control={form.control}
                        name="pin"
                        render={({field}) => (
                            <FormItem>
                                <FormLabel className="text-center">{t("twoFactorAuthPage.title")}</FormLabel>
                                <FormControl>
                                    <InputOTP maxLength={8} {...field} className="justify-center">
                                        <InputOTPGroup className="flex justify-center">
                                            {Array.from({length: 8}).map((_, index) => (
                                                <InputOTPSlot key={index} index={index}/>
                                            ))}
                                        </InputOTPGroup>
                                    </InputOTP>
                                </FormControl>
                                <FormDescription className="text-center">
                                    {t("twoFactorAuthPage.info")}
                                </FormDescription>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <Button type="submit" className="w-full pb-2"
                            disabled={isLoading}>
                        {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                            </>
                        ) : (
                            t("twoFactorAuthPage.submit")
                        )}
                    </Button>
                </form>
            </Form>
        </div>
    );
}

export default TwoFactorAuthPage;
